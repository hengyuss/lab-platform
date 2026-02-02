import json
import logging

from app.core.config import settings
from app.services.crawl_dblp import DblpXmlFetcher

logger = logging.getLogger('uvicorn')
fetcher = DblpXmlFetcher()
def process_mq_message(ch, method, properties, body, rabbitmq):
  try:
    msg_str = body.decode('utf-8')
    task_data = json.loads(msg_str)
    teacher_name = task_data.get('name')
    logger.info(f"处理中， 收到 任务{teacher_name}")
    teacher_pid = settings.TEACHER_PID_JSON.get(teacher_name)

    if not teacher_name or not teacher_pid:
      logger.info(f"没有{teacher_name} 的 pid信息 无法处理")
      ch.basic_ack(delivery_tag=method.delivery_tag)
      return

    data = fetcher.fetch(teacher_pid)

    results = {
      "teacher_name": teacher_name,
      "pid": teacher_pid,
      "data": data,
      "count": len(data)
    }

    rabbitmq.publish(settings.MQ_QUEUE_RESULT, results)

    ch.basic_ack(delivery_tag=method.delivery_tag)
    logger.info(f"✅ [完成] {teacher_name} - 抓取 {len(data)} 篇\n result:{results}")

  except Exception as e:
    logger.error(f"❌ 处理异常: {e}")
    ch.basic_ack(delivery_tag=method.delivery_tag)