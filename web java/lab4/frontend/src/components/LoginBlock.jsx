import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { login, clearLoginError } from '../store/auth/authActions';

const LoginBlock = () => {
  const dispatch = useDispatch();
  const error = useSelector(state => state.auth.loginError);

  const [loginValue, setLoginValue] = useState('');
  const [passwordValue, setPasswordValue] = useState('');

  const handleLogin = (e) => {
		e.preventDefault();
		dispatch(login(loginValue, passwordValue));
  };
  
  const onChangeLogin = (e) => {
		setLoginValue(e.target.value);
		dispatch(clearLoginError());
  };
  const onChangePassword = (e) => {
		setPasswordValue(e.target.value);
		dispatch(clearLoginError());
  };

  return (
    <div className="auth-block">
		<h2>Вход</h2>
		{error && <p className="error">{error}</p>}
		<form onSubmit={handleLogin}>
			<input
				type="text"
				placeholder="Логин"
				value={loginValue}
				onChange={onChangeLogin}
				required
			/>
			<input
				type="password"
				placeholder="Пароль"
				value={passwordValue}
				onChange={onChangePassword}
				required
			/>
			<button type="submit">Войти</button>
		</form>
    </div>
  );
};

export default LoginBlock;