import { useEffect, useCallback } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link } from 'react-router-dom'; 
import { logout } from '../store/auth/authActions';
import { loadResults, clearResults } from '../store/app/appActions';
import GraphBlock from '../components/GraphBlock';
import PointForm from '../components/PointForm';
import ResultsTableBlock from '../components/ResultsTableBlock';
import '../pages/styles/main.css';


const MainPage = () => {
  const dispatch = useDispatch();
  
  const {r} = useSelector(state => state.app);
  
  useEffect(() => {
	dispatch(loadResults());
  }, [dispatch]);
  
  const onClickLogoutBtn = useCallback(async (e) => {
		e.preventDefault();
		dispatch(logout());
  }, [dispatch]);
  const onClickDeleteBtn = (e) => {
		dispatch(clearResults());
  };

  return (
	<div className="container">
		<div className="left">
			<GraphBlock R={r}/>
		</div>
		<div className="right">
			<div  className="param-block">
				<PointForm />
				<div className="buttons-block">
					<button
						className="delete-btn"
						onClick={onClickDeleteBtn}
					>
						Очистить историю
					</button>
					<Link
						to="/lab4/"
						className="logout-btn"
						onClick={onClickLogoutBtn}
					>
						выйти из аккаунта
					</Link>
				</div>
			</div>
			<ResultsTableBlock />
			
		</div>
		
	</div>
  );
};

export default MainPage;