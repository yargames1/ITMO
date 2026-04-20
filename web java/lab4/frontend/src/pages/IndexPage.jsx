import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { Navigate } from 'react-router-dom';

import RegBlock from '../components/RegBlock';
import LoginBlock from '../components/LoginBlock';
import HeaderBlock from '../components/HeaderBlock';

import { fetchMe } from '../store/auth/authActions';

import './styles/auth.css';

const IndexPage = () => {
	const {isAuth} = useSelector(state => state.auth);
	const [mode, setMode] = useState('login');

	if (isAuth) {
		return <Navigate to="/app" replace/>;
	}

	return (
		<div className="page">
		<HeaderBlock />
	
		{mode === 'login' ? (
			<div className="auth-page">
				<LoginBlock />
				<button
				  className="switch-mode-btn"
				  onClick={()=> setMode('register')}
				>
				Или зарегистрируйтесь, если нет аккаунта
				</button>
			</div>
			) : (
			<div className="auth-page">
				<RegBlock />
				<button
				  className="switch-mode-btn"
				  onClick={()=> setMode('login')}
				>
				Или войдите в аккаунт
				</button>
			</div>
		)}

		</div>
	);
};

export default IndexPage;