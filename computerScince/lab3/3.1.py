json_text = open("расписание.json", 'r', encoding='utf-8')

xml_text = '<?xml version="1.0" encoding="UTF-8"?>\n'

check = False

tags = ['root']
for line in json_text.readlines():
    if '{' not in line and '}' not in line:
        tag = '</'
        tag_check = True
        for s in line:
            if s == '\"' and tag_check:
                check = not check
                xml_text += '<' if check else '>'
            elif s == '\"':
                pass
            elif s ==':' and not check:
                tag_check = not tag_check
            elif check and tag_check:
                tag += s
                xml_text += s
            elif s == '\n':
                xml_text += tag+'>'
                xml_text += s
            elif s == ',' and not check:
                pass
            else:
                xml_text += s
    elif ': {' in line:
        tags_check = False
        tab_check = True
        new_tag = ''
        for s in line:
            if s == '\"':
                tags_check = not tags_check
                tab_check = False
            elif tags_check:
                new_tag += s
            if tab_check:
                xml_text += s
        tags.append(new_tag)
        xml_text += f"<{new_tag}>\n"
    elif '}' in line:
        tab_check2 = True
        for s in line:
            if s == '}':
                tab_check2 = False
            if tab_check2:
                xml_text += s
        xml_text += f"</{tags.pop(len(tags)-1)}>\n"
    elif '{' in line:
        xml_text += f"<{tags[0]}>\n"
    else:
        xml_text += line

with open("расписание.xml", "w", encoding='utf-8') as f:
    f.write(xml_text)
