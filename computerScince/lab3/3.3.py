import re

json_text = open("расписание json.txt", 'r', encoding='utf-8')

xml_text = '<?xml version="1.0" encoding="UTF-8"?>\n'

check = False

tags = ['root']
for line in json_text.readlines():
    if '{' not in line and '}' not in line:
        tab = re.search(r'\s*\"', line)[0][:-1]
        tag = re.search(r'\".*\"\s:', line)[0][1:-3]
        data = re.search(r': \".*\"', line)[0][3:-1]
        xml_text += f"{tab}<{tag}>{data}</{tag}>\n"
    elif ': {' in line:
        tab = re.search(r'\s*\"', line)[0][:-1]
        tag = re.search(r'\".*\"?: ', line)[0][1:-4]
        tags.append(tag)
        xml_text += f"{tab}<{tag}>\n"
    elif '}' in line:
        tab = re.search(r'.*}', line)[0][:-1]
        xml_text += f"{tab}</{tags.pop(len(tags)-1)}>\n"
    elif '{' in line:
        xml_text += f"<{tags[0]}>\n"
    else:
        xml_text += line

with open("расписание.xml", "w", encoding='utf-8') as f:
    f.write(xml_text)



