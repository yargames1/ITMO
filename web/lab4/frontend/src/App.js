import { useDispatch, useSelector } from 'react-redux';
import { useEffect } from "react";
import { Navigate, Routes, Route } from 'react-router-dom';
import IndexPage from './pages/IndexPage';
import MainPage from './pages/MainPage';
import { fetchMe } from './store/auth/authActions';

function App() {
	const dispatch = useDispatch();
	const {isAuth, loading} = useSelector(state => state.auth);
	
	useEffect(() => {
		dispatch(fetchMe());
	}, [dispatch]);
	
	if (loading) {
		return <div>Loading...</div>;
	}

	return (
		<Routes>
			<Route path="/" element={<IndexPage />} />
			<Route path="/app" element={isAuth ? <MainPage /> : <Navigate to="/" />} />
		</Routes>
	);
}

export default App;
