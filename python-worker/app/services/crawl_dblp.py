import logging
import xml.etree.ElementTree as ET
from typing import List, Dict, Any

import requests

logger = logging.getLogger("uvicorn")

class DblpXmlFetcher:
  def __init__(self):
    self.headers = {
      "User-Agent": "LabPlatformWorker/1.0 (mailto:1183660933@qq.com)"
    }
  def fetch(self, pid: str) -> List[Dict[str, Any]]:
    url = f"https://dblp.org/pid/{pid}.xml"

    try:
      resp = requests.get(url, headers=self.headers, timeout=60)
      if resp.status_code != 200:
        print(f"dblp爬取错误, code: {resp.status_code}, pid: {pid}")
        return []
      return self._parse_xml(resp.content)
    except Exception as e:
      print(f"抓取异常 ({pid}): {e}")
      return []

  def _parse_xml(self, xml_content: bytes) -> List[Dict[str, Any]]:
    papers_data = []

    try:
      root = ET.fromstring(xml_content)

      # DBLP XML 结构: <dblpperson> -> <r> -> <article/inproceedings...>
      records = root.findall("./r")

      for r in records:
        # <r> 标签下通常只有一个子节点 (具体的论文条目)
        if len(r) == 0: continue
        paper_node = r[0]

        # --- 1. 提取基础信息 ---
        title = self._get_text(paper_node, "title", "无标题")
        year = self._get_text(paper_node, "year", "????")

        # --- 2. 提取出处 (会议 或 期刊) ---
        # 会议通常在 <booktitle>，期刊在 <journal>
        venue = self._get_text(paper_node, "booktitle")
        dblp_key = paper_node.get("key", "")
        if not venue:
          venue = self._get_text(paper_node, "journal", "未知来源")

        # --- 3. 提取下载链接 (EE) ---
        # 有些论文有多个 EE，我们取第一个 (通常是 DOI)
        ee_node = paper_node.find("ee")
        ee_link = ee_node.text if ee_node is not None else ""

        # --- 4. 提取卷号 (Volume) 和 页码 (Pages) ---
      # 新增解析逻辑
        volume = self._get_text(paper_node, "volume", "")
        pages = self._get_text(paper_node, "pages", "")

        # --- 4. 提取作者列表 ---
        # XML 的好处：作者永远是多个 <author> 标签，不用像 JSON 那样判断类型
        authors = [
          auth.text for auth in paper_node.findall("author")
          if auth.text
        ]

        # --- 5. 组装数据 ---
        papers_data.append({
          "title": title,
          "dblp_key": dblp_key,
          "year": year,
          "venue": venue,
          "volume": volume,
          "pages": pages,
          "ee": ee_link,
          "authors": authors,
          "type": paper_node.tag  # 记录是 article 还是 inproceedings
        })

    except ET.ParseError:
      print("❌ XML 解析失败，文件可能损坏")

    return papers_data

  @staticmethod
  def _get_text(element: ET.Element, tag: str, default: str = "") -> str:
    """安全获取节点文本，防止 None 报错"""
    child = element.find(tag)
    if child is not None and child.text:
      return child.text
    return default




if __name__ == "__main__":
  fetcher = DblpXmlFetcher()
  pid = "195/3356"
  results = fetcher.fetch(pid)
  print(results)


