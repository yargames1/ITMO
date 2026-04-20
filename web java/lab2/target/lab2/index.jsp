<% /* Страница JSP, формирующая HTML-страницу с веб-формой.
Должна обрабатывать все запросы, не содержащие сведений
о координатах точки и радиусе области. */ %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <style>
        body {
            font-family: sans-serif;
            color: black;
        }
        #header {
            font-family: monospace;
            color: blue;
            font-size: 18px;
            margin-bottom: 2%;
        }

        .container {
            display: flex;
        }

        #graph {
            width: 50%;
            height: 100%;
            margin: 20px;
        }

        #rightPart {
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: flex-start; // чтобы не прижималось вправо
            padding: 20px;
            box-sizing: border-box;
        }

        .box {
            padding: 20px;
            background: #fff;
            border: 1px solid #ccc;
            margin-bottom: 20px;
            display: flex;
            flex-direction: row;
            gap: 15px;
        }

        .group.vertical {
            display: flex;
            flex-direction: column;
        }

        input, select, button {
            margin: 2%;
            padding: 1%;
        }

        button:hover {
            background-color: lightblue;
        }

        tbody {
            font-family: monospace;
            color: black;
            font-size: 18px;
            border: 3px solid;
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            padding: 10px;
            border: 1px solid black;
            text-align: center;
        }

        input[name="x"].selected {
            background-color: #4f4fff;
            color: white;
            border: 1px solid #3333a3;
        }
    </style>
</head>
<body>

    <header id="header">
        Немыкин Ярослав Алексеевич; группа P3222; Вариант 58999
    </header>

    <div class="container">
        <canvas id="graph" height="500" width="500" style="border:1px solid black; height: 500 px; width: 500 px;"></canvas>

        <div id="rightPart">
                <form id="mainForm" class="box" action="/lab2/controller" method="post">
                    <div class="group vertical">
                        <label>Определите занчение Х:</label><br>
                        <input type="button" name="x" value="-5" onclick="updateX(-5); validateForm()"><br>
                        <input type="button" name="x" value="-4" onclick="updateX(-4); validateForm()"><br>
                        <input type="button" name="x" value="-3" onclick="updateX(-3); validateForm()"><br>
                        <input type="button" name="x" value="-2" onclick="updateX(-2); validateForm()"><br>
                        <input type="button" name="x" value="-1" onclick="updateX(-1); validateForm()"><br>
                        <input type="button" name="x" value="0" onclick="updateX(0); validateForm()"><br>
                        <input type="button" name="x" value="1" onclick="updateX(1); validateForm()"><br>
                        <input type="button" name="x" value="2" onclick="updateX(2); validateForm()"><br>
                        <input type="button" name="x" value="3" onclick="updateX(3); validateForm()">
                    </div>

                    <div class="group vertical">
                        <label for="y">Определите значение Y:</label>
                        <input type="text" id="y" name="y" placeholder="{-3 ... 5}" onkeydown="return event.key !== 'Enter'">
                        <span id="yError" style="color:red; font-size:14px"></span>
                    </div>

                    <div class="group vertical">
                        <label for="r">Определите значение R:</label>
                        <select id="r" name="r">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                    </div>

                    <button type="button" id="submitBtn" onclick="sendFormByButton()">Отправить</button>

                </form>


            <table id="resultTable">
                <thead>
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Попадание</th>
                    <th>Время сервера</th>
                    <th>Время обработки</th>
                </tr>
                </thead>
                <tbody>
                    <c:forEach var="record" items="${applicationScope.history}">
                        <tr>
                            <td>${record.x}</td>
                            <td>${record.y}</td>
                            <td>${record.r}</td>
                            <td><c:choose>
                                    <c:when test="${record.hit}">Да</c:when>
                                    <c:otherwise>Нет</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${record.serverTimeMillis}</td>
                            <td>${record.processingTimeMillis}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

        </div>
    </div>

    <script>
        const submitBtn = document.getElementById("submitBtn");
        const form = document.getElementById("mainForm");
        const yInput = document.getElementById("y");
        let xValue;
        const yError = document.getElementById("yError");

        const canvas = document.getElementById('graph');
        const ctx = canvas.getContext('2d');

        const width = canvas.width;
        const height = canvas.height;

        let fakeR = false;
        let xSelected = false;

        function updateX(newX){
            xValue = newX;
            xSelected = true;
            // Убираем подсветку со всех кнопок
            document.querySelectorAll('input[name="x"]').forEach(btn => {
                btn.classList.remove('selected');
            });

            // Подсвечиваем выбранную кнопку
            const selectedButton = document.querySelector(`input[name="x"][value="\${newX}"]`);
            if (selectedButton) {
                selectedButton.classList.add('selected');
            }
        }

        function sendFormByButton(){
            const xInput = document.createElement('input');
            xInput.type = 'hidden';
            xInput.name = 'x';
            xInput.value = `\${xValue}`;
            form.appendChild(xInput);
            form.submit();
        }

        function fillTableByPoint(data){
            let resultTableBody = document.querySelector('#resultTable tbody');

            let row = document.createElement('tr');

            // создаем ячейки и добавляем их в строку
            let xCell = document.createElement('td');
            xCell.textContent = data.x;
            row.appendChild(xCell);

            let yCell = document.createElement('td');
            yCell.textContent = data.y;
            row.appendChild(yCell);

            let rCell = document.createElement('td');
            rCell.textContent = data.r;
            row.appendChild(rCell);

            let hitCell = document.createElement('td');
            hitCell.textContent = data.hit ? 'Да' : 'Нет';
            row.appendChild(hitCell);

            let serverTimeCell = document.createElement('td');
            serverTimeCell.textContent = data.serverTime;
            row.appendChild(serverTimeCell);

            let procTimeCell = document.createElement('td');
            procTimeCell.textContent = data.procTime;
            row.appendChild(procTimeCell);

            if (resultTableBody.firstChild) {
                resultTableBody.insertBefore(row, resultTableBody.firstChild);
            }
            else {
                resultTableBody.appendChild(row); // если таблица пустая
            }
        }

        async function sendForm(x, y, r) {
            const formData = new URLSearchParams();
            formData.append("x", x);
            formData.append("y", y);
            formData.append("r", r);
            formData.append("byClick", "True");
            const response = await fetch("controller", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: formData.toString()
            }
            );
            const htmlResponse = await response.json();
            // Обращаемся для обновления истории!
            fillTableByPoint(htmlResponse);
        }

        function drawPoint(x, y, r){
            let cx = canvas.width / 2;
            let cy = canvas.height / 2;
            let scale = (cx-25)/r;
            ctx.fillStyle = `red`;
            ctx.beginPath();
            ctx.arc(cx+scale*x, cx-scale*y, 5, 0, Math.PI * 2, true);
            ctx.fill();
            ctx.closePath();
        }

        function updateGraph(R) {
            let cx = canvas.width / 2;
            let cy = canvas.height / 2;
            R = Number(R);
            fakeR = false;
            if (isNaN(R)){
                R = 1;
                fakeR = true;
            }
            let scale = (cx-25)/R;
            ctx.clearRect(0, 0, width, height);
            ctx.strokeStyle = 'black';
            ctx.lineWidth = 1;
            ctx.closePath();
            ctx.fillStyle = 'blue';
            // Треугольник
            ctx.beginPath();
            ctx.moveTo(cx, cy);
            ctx.lineTo(cx+R/2*scale, cy);
            ctx.lineTo(cx, cy+R/2*scale);
            ctx.closePath();
            ctx.fill();
            // квадрат
            ctx.fillRect(cx-R*scale, cy, R*scale, R*scale);
            // четверть-круг
            ctx.beginPath();
            ctx.arc(cx, cy, R*scale, Math.PI, Math.PI/2*3, false);
            ctx.lineTo(cx, cy);
            ctx.fill();
            ctx.closePath();
            // Оси
            ctx.beginPath();
            ctx.moveTo(0, cy);
            ctx.lineTo(width, cy); // X
            ctx.moveTo(cx, 0);
            ctx.lineTo(cy, height); // Y
            ctx.stroke();
            // Деления шкалы
            ctx.fillStyle = 'black';
            if (fakeR){
                for(let i=-2*R; i<=2*R; i++) {
                    if(i===0) continue;
                    ctx.fillRect(cx + i*scale/2 - 1, cy - 3, 2, 6);
                    ctx.fillRect(cx - 3, cy - i*scale/2 - 1, 6, 2);
                    ctx.fillText(i/2+"R", cx + i*scale/2 - 10, cy + 15);
                    ctx.fillText(i/2+"R", cx + 10, cy - i*scale/2 + 3);
                }
            }
            else{
                for(let i=-2*R; i<=2*R; i++) {
                    if(i===0) continue;
                    ctx.fillRect(cx + i*scale/2 - 1, cy - 3, 2, 6);
                    ctx.fillRect(cx - 3, cy - i*scale/2 - 1, 6, 2);
                    ctx.fillText(i/2, cx + i*scale/2 - 10, cy + 15);
                    ctx.fillText(i/2, cx + 10, cy - i*scale/2 + 3);
                }
            }
        }

        function handleCanvasClick(event){
            let rect = canvas.getBoundingClientRect();

            let cx = canvas.width / 2;
            let cy = canvas.height / 2;

            let clickX = (event.clientX - rect.left)/((rect.right-rect.left)/2)*cx;
            let clickY = (event.clientY - rect.top)/((rect.bottom-rect.top)/2)*cy;

            let r = parseFloat(document.getElementById("r").value);

            if (fakeR) {
                alert("Сначала выберите значение r!");
                return;
            }

            let scale = (cx-25)/r;
            let x = (clickX - cx) / scale;
            let y = (cy - clickY) / scale;

            // Округлим на всякий
            let xRounded = Math.round(x * 100) / 100;
            let yRounded = Math.round(y * 100) / 100;

            drawPoint(xRounded, yRounded, r);
            // Отправить инфу на сервак, дать точку на лист
            sendForm(xRounded, yRounded, r);
        }

        // валидация
        function validateForm() {
            let yStr = yInput.value.trim();
            let yValid = /^-?\d+(\.\d+)?$/.test(yStr) && parseFloat(yStr) >= -3 && parseFloat(yStr) <= 5;

            if (!yValid && yStr !== "") {
                yError.textContent = "Y - число в диапазоне [-3; 5]";
            } else {
                yError.textContent = "";
            }

            submitBtn.disabled = !(xSelected && yValid);
        }


        function updateR(){
            validateForm();
            updateGraph(document.getElementById("r").value)
        }


        // Старт работы
        updateGraph(NaN);
        validateForm();


        // обновление при изменении
        canvas.addEventListener("click", handleCanvasClick);
        // xInputs.forEach(bt =>bt.addEventListener("click", validateForm));
        yInput.addEventListener("input", validateForm);
        document.getElementById("r").addEventListener("change", updateR);



    </script>

</body>
