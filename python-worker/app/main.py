import logging
import threading
from contextlib import asynccontextmanager
from functools import partial

import uvicorn
from fastapi import FastAPI

from app.mq.mq_paper_consumer import process_mq_message
from app.mq.rabbitmq import RabbitMQService

logger = logging.getLogger("uvicorn")


mq_service = RabbitMQService()
test_service = RabbitMQService()

def run_consumer_in_background():
  callback_with_service = partial(process_mq_message, rabbitmq=mq_service)
  mq_service.consume_loop(callback_with_service)

@asynccontextmanager
async def lifespan(app: FastAPI):
  logger.info(f"fast 服务启动")
  consumer_thread = threading.Thread(target=run_consumer_in_background, daemon=True)
  consumer_thread.start()
  logger.info(f"MQ 服务已启动")
  yield
  logger.info(f"服务停止中")
  mq_service.stop()

app = FastAPI(title="Lab Platform Python Worker", lifespan=lifespan)

@app.post("/test-trigger")
def manual_trigger():
  """
  示例：除了自动监听 MQ，你也可以通过 HTTP 手动触发某些逻辑
  """
  # 比如手动往队列里塞一个任务测试一下
  task = {"name": "Yong Ding"}
  test_service.publish("paper_meta_queue", task)
  return {"message": "任务已手动发送到队列", "task": task}

if __name__ == "__main__":
  uvicorn.run(app, host="0.0.0.0", port=9900)
