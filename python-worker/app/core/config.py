import json
from pathlib import Path
from typing import List, Dict, Any

from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict


def load_teacher_json() -> List[Dict[str, Any]]:
  base_dir = Path(__file__).resolve().parent.parent.parent
  json_path = base_dir / "teacher_dblp_pid.json"
  try:
    if json_path.exists():
      with open(json_path, "r", encoding="utf-8") as f:
        return json.load(f)
    else:
      print(f"teacher_dblp_pid 配置文件未找到")
      return []
  except Exception as e:
    print(f"json 加载出错")
    return []


class Settings(BaseSettings):
  RABBITMQ_HOST: str = "localhost"

  RABBITMQ_PORT: int = 5672
  RABBITMQ_USERNAME: str = "admin"
  RABBITMQ_PASSWORD: str = "z15903750100"

  MQ_QUEUE_TASK: str = "paper_meta_queue"
  MQ_QUEUE_RESULT: str = "paper_meta_result_queue"


  DBLP_SEARCH_LIMIT: int = 10
  TEACHER_PID_JSON: Dict[str, str] = Field(default_factory=load_teacher_json)

  DBLP_BASE_URL: str = "https://dblp.org/search/publ/api"
  model_config = SettingsConfigDict(
    env_file=".env",
    env_file_encoding="utf-8",
    extra="ignore"
  )

settings = Settings()
print(settings.TEACHER_PID_JSON)


