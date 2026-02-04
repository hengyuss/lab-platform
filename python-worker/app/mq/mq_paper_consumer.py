import json
import logging
import random
import time
from typing import Dict, Any

from app.core.config import settings
from app.services.crawl_dblp import DblpXmlFetcher

logger = logging.getLogger('uvicorn')
fetcher = DblpXmlFetcher()
def process_mq_message(ch, method, _properties, body, rabbitmq):
  try:
    msg_str = body.decode('utf-8')
    task_data = json.loads(msg_str)
    logger.info(f"task_data: {task_data}")
    teacher_name = task_data.get('teacher_name')
    logger.info(f"处理中， 收到 任务{teacher_name}")

    if teacher_name == "all":
      for name, pid in settings.TEACHER_PID_JSON.items():
        results = handle_message(name, pid)
        send_message(ch, method, rabbitmq, results)
        logger.info(f"✅ [完成] {teacher_name} - 抓取 {results["data"]} 篇\n result:{results}")
    else:
      teacher_pid = settings.TEACHER_PID_JSON.get(teacher_name)
      results = handle_message(teacher_name, teacher_pid)
      send_message(ch, method, rabbitmq, results)
      logger.info(f"✅ [完成] {teacher_name} - 抓取 {results["data"]} 篇\n result:{results}")
    ch.basic_ack(delivery_tag=method.delivery_tag)

  except Exception as e:
    logger.error(f"❌ 处理异常: {e}")

def handle_message(teacher_name: str, teacher_pid: str) -> Dict[str, Any]:
  results = {
    "status": False,
    "teacher_name": teacher_name,
    "pid": teacher_pid
  }

  if not teacher_name or not teacher_pid:
    logger.warning(f"没有{teacher_name} 的 pid信息 无法处理")
    return results

  wait_time = random.uniform(1.0, 3.0)
  logger.info(f"爬取一个老师数据后休息{wait_time}s, 防止ip被封")
  time.sleep(wait_time)
  data = fetcher.fetch(teacher_pid)

  results["status"] = True
  results["count"] = len(data)
  results["data"] = data

  return results

def send_message(_ch, _method, rabbitmq, message):


  rabbitmq.publish(settings.MQ_QUEUE_RESULT, message)



