import { useEffect, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { addResult } from '../store/app/appActions';

const GraphBlock = ({R}) => {
	const dispatch = useDispatch();
	
	const results = useSelector(state => state.app.results);
	
	const canvasRef = useRef(null);
	
	function drawGraph(ctx, width, height, R) {
	  const cx = width / 2;
	  const cy = height / 2;

	  let fakeR = false;
	  R = Number(R);

	  if (isNaN(R) || R === 0) {
		R = 1;
		fakeR = true;
	  }

	  const scale = (cx - 25) / R;

	  ctx.clearRect(0, 0, width, height);

	  ctx.fillStyle = 'rgba(0, 0, 255, 0.5)';

	  // Треугольник
	  ctx.beginPath();
	  ctx.moveTo(cx, cy);
	  ctx.lineTo(cx - R/2 * scale, cy);
	  ctx.lineTo(cx, cy + R * scale);
	  ctx.closePath();
	  ctx.fill();

	  // Квадрат
	  ctx.fillRect(cx, cy, R/2 * scale, -R * scale);

	  // Четверть круга
	  ctx.beginPath();
	  ctx.arc(cx, cy, R * scale, 0, Math.PI / 2);
	  ctx.lineTo(cx, cy);
	  ctx.fill();

	  ctx.strokeStyle = 'black';
	  ctx.lineWidth = 1;

	  ctx.beginPath();
	  ctx.moveTo(0, cy);
	  ctx.lineTo(width, cy);
	  ctx.moveTo(cx, 0);
	  ctx.lineTo(cx, height);
	  ctx.stroke();

	  ctx.fillStyle = 'black';
	  ctx.font = '12px sans-serif';

	  for (let i = -2 * R; i <= 2 * R; i++) {
		if (i === 0) continue;

		const x = cx + (i * scale) / 2;
		const y = cy - (i * scale) / 2;

		// Засечки
		ctx.fillRect(x - 1, cy - 3, 2, 6);
		ctx.fillRect(cx - 3, y - 1, 6, 2);

		const label = fakeR ? `${i / 2}R` : i / 2;

		ctx.fillText(label, x - 10, cy + 15);
		ctx.fillText(label, cx + 8, y + 4);
	  }
	}
	
	function drawPoint(ctx, x, y, r, width, height) {
		let cx = width / 2;
		let cy = height / 2;
		let scale = (cx - 25) / r;

		if ((x <= 0 && y <= 0 && y >= -2 * x - r) ||
			(x >= 0 && y >= 0 && (x <= r/2 && y <= r)) ||
			(x >= 0 && y <= 0 && (x * x + y * y <= r * r))) {
			ctx.fillStyle = 'green';
		} else {
			ctx.fillStyle = 'red';
		}

		ctx.beginPath();
		ctx.arc(cx + scale * x, cy - scale * y, 5, 0, Math.PI * 2, true);
		ctx.fill();
		ctx.closePath();
	}
	
	const handleCanvasClick = async e =>{
		if (!R || R === 0) {
			alert("Сначала выберите значение r!");
			return;
		}
		const canvas = canvasRef.current;
		const rect = canvas.getBoundingClientRect();

		const cx = canvas.width / 2;
		const cy = canvas.height / 2;

		const clickX = (e.clientX - rect.left)/((rect.right-rect.left)/2)*cx;
		const clickY = (e.clientY - rect.top)/((rect.bottom-rect.top)/2)*cy;


		const scale = (cx-25)/R;
		const x = (clickX - cx) / scale;
		const y = (cy - clickY) / scale;

		// Округлим на всякий
		const xRounded = Math.round(x * 100) / 100;
		const yRounded = Math.round(y * 100) / 100;

		// Рисуем-с
		const ctx = canvas.getContext('2d');
		drawPoint(ctx, xRounded, yRounded, R, canvas.width, canvas.height);

		// делаем отправку
		try {
		const response = await fetch('/lab4/api/points', {
		  method: 'POST',
		  headers: { 'Content-Type': 'application/json' },
		  credentials: 'include',
		  body: JSON.stringify({ x: xRounded, y: yRounded, r: R })
		});

		if (!response.ok) {
		  throw new Error('Ошибка отправки точки');
		}

		const result = await response.json();
		dispatch(addResult(result));
	  } catch (err) {
		alert(err.message);
	  }
	}
	
	useEffect( () => {
		const canvas = canvasRef.current;
		if (!canvas) return;
		
		const ctx = canvas.getContext('2d');
		
		drawGraph(ctx, canvas.width, canvas.height, R);
		results.forEach(point => {
		if (point.r === R) {
		  drawPoint(
			ctx,
			point.x,
			point.y,
			point.r,
			canvas.width,
			canvas.height
		  );
		}
	  });
}, [R, results]);
  
	return (
		<canvas
			ref={canvasRef}
			width={500}
			height={500}
			className="graph"
			onClick={handleCanvasClick}
		/>
	);
};

export default GraphBlock;