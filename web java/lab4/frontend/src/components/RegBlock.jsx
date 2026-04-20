import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { register, clearRegisterError } from '../store/auth/authActions';

const RegisterBlock = () => {
  const dispatch = useDispatch();
  const error = useSelector(state => state.auth.registerError);

  const [loginValue, setLoginValue] = useState('');
  const [passwordValue, setPasswordValue] = useState('');

  const handleRegister = (e) => {
	e.preventDefault();
	dispatch(register(loginValue, passwordValue));
  };
  
  const onChangeLogin = (e) => {
		setLoginValue(e.target.value);
		dispatch(clearRegisterError());
  };
  const onChangePassword = (e) => {
		setPasswordValue(e.target.value);
		dispatch(clearRegisterError());
  };

  return (
    <div className="auth-block">
		<h2>Регистрация</h2>
		{error && <p className="error">{error}</p>}
		<form onSubmit={handleRegister}>
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
			<button type="submit">Зарегистрироваться</button>
		</form>
    </div>
  );
};

export default RegisterBlock;