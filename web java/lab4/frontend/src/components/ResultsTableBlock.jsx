import { useSelector } from 'react-redux';

const ResultsTableBlock = () => {
  const results = useSelector(state => state.app.results);

  return (
	<table className="results">
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
		{results.length === 0 ? (
		  <tr>
			<td colSpan="6">Нет данных</td>
		  </tr>
		) : (
		  results.map((result, index) => (
			<tr key={index}>
			  <td>{result.x}</td>
			  <td>{result.y}</td>
			  <td>{result.r}</td>
			  <td>{result.hit ? 'Да' : 'Нет'}</td>
			  <td>{new Date(result.serverTime).toLocaleString()}</td>
			  <td>{result.processingTime} мс</td>
			</tr>
		  ))
		)}
	  </tbody>
	</table>
  );
};

export default ResultsTableBlock;