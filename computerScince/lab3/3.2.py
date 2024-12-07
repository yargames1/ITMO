from json2xml import json2xml
from json2xml.utils import readfromstring

json_text = open("расписание json.txt", 'r', encoding='utf-8')
json_data = readfromstring(json_text.read())
xml_text = json2xml.Json2xml(json_data).to_xml()

with open("расписание.xml", "w", encoding='utf-8') as f:
    f.write(xml_text)

