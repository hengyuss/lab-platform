import json
from venv import logger

import pika
from pika.credentials import PlainCredentials

from app.core.config import settings


class RabbitMQService:
  def __init__(self):
    self._connection = None
    self._channel = None
    self.is_connected = False

  def connect(self):
    credentials = PlainCredentials(
      username=settings.RABBITMQ_USERNAME,
      password=settings.RABBITMQ_PASSWORD
    )
    try:
      self._connection = pika.BlockingConnection(
        pika.ConnectionParameters(
          host = settings.RABBITMQ_HOST,
          port = settings.RABBITMQ_PORT,
          credentials=credentials
        )
      )

      self._channel = self._connection.channel()
      self._channel.queue_declare(queue=settings.MQ_QUEUE_TASK, durable=True)
      self._channel.queue_declare(queue=settings.MQ_QUEUE_RESULT, durable=True)

      self._channel.basic_qos(prefetch_count=1)
      self.is_connected=True
      logger.info(f"RabbitMQ 连接成功")
    except Exception as e:
      logger.error(f"RabbitMQ 连接失败")
      self.is_connected = False

  def publish(self, queue_name: str, message: dict):
    if not self.is_connected:
      self.connect()
    try:
      self._channel.basic_publish(
        exchange='',
        routing_key=queue_name,
        body=json.dumps(message, ensure_ascii=False),
        properties=pika.BasicProperties(delivery_mode=2)
      )
    except Exception as e:
      logger.error(f"发送失败 {e}")

  def consume_loop(self, callback_func):
    if not self.is_connected:
      self.connect()

    self._channel.basic_consume(
      queue=settings.MQ_QUEUE_TASK,
      on_message_callback=callback_func,
      auto_ack=False
    )
    logger.info(f"开始监听队列 {settings.MQ_QUEUE_TASK}")
    try:
      self._channel.start_consuming()
    except Exception as e:
      logger.error(f"监听中断 {e}")

  def stop(self):
    if self._connection and self._connection.is_open:
      logger.info(f"关闭mq连接")
      self._connection.add_callback_threadsafe(self._channel.stop_consuming)
      self._connection.close()
