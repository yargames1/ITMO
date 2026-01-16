import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setX, setY, setR, addResult } from '../store/app/appActions';

const X_VALUES = [-3, -2, -1, 0, 1, 2, 3, 4, 5];
const R_VALUES = [1, 2, 3, 4, 5];

const PointForm = () => {
	const dispatch = useDispatch();
	const { x, y, r } = useSelector(state => state.app);

	// Валидация
	const isValidY = value => {
		if (value === '') return false;
		const num = Number(value);
		return !isNaN(num) && num >= -3 && num <= 3;
	};

	// Отправка формы
	const sendPoint = async (x, y, r) => {
		const response = await fetch('/lab4/api/points', {
			method: 'POST',
			headers: {
			  'Content-Type': 'application/json'
			},
			credentials: 'include',
			body: JSON.stringify({ x, y, r })
		});

		if (!response.ok) {
			throw new Error('Ошибка при отправке точки');
		}

		return response.json();
	};

	const handleSubmit = async e => {
		e.preventDefault();

		if (x === null) {
			alert('Выберите X');
			return;
		}

		if (!isValidY(y)) {
			alert('Y должен быть числом от -3 до 3');
			return;
		}

		if (r === 0) {
			alert('Выберите R');
			return;
		}
		
		try {
			const result = await sendPoint(x, Number(y), r);
			dispatch(addResult(result));
		} catch (err) {
			alert(err.message);
		}
	}


	return (
	<form className="box" onSubmit={handleSubmit}>
		<h3>Параметры точки</h3>

		<div className="x-line">
		<label>X:</label>
		<div>
			{X_VALUES.map(val => (
				<label key={val}>
				  <input
					type="radio"
					name="x"
					value={val}
					checked={x === val}
					onChange={() => dispatch(setX(val))}
				  />
				  {val}
				</label>
			))}
		</div>
		</div>

		<div className="y-line">
		<label>Y:</label>
		<input
			type="text"
			placeholder="-3 ... 3"
			value={y}
			onChange={e => dispatch(setY(e.target.value))}
			className={y !== '' && !isValidY(y) ? 'error' : ''}
		/>
		</div>

		<div className="r-line">
		<label>R:</label>
		<div>
		  {R_VALUES.map(val => (
			<label key={val}>
			  <input
				type="radio"
				name="r"
				value={val}
				checked={r === val}
				onChange={() => dispatch(setR(val))}
			  />
			  {val}
			</label>
		  ))}
		</div>
		</div>

		<button type="submit">Проверить</button>
	</form>
	);
};

export default PointForm;