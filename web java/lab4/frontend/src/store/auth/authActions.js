import {
  LOGIN_SUCCESS,
  LOGIN_ERROR,
  CLEAR_LOGIN_ERROR,
  REGISTER_ERROR,
  CLEAR_REGISTER_ERROR,
  SET_USER,
  LOGOUT
} from './authTypes';

/*
 Логин
*/
export const login = (login, passwd) => async dispatch => {
	try {
		const response = await fetch('/lab4/api/main/login', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			credentials: 'include',
			body: JSON.stringify({ login, passwd })
	});

	if (!response.ok) {
		const errorText = await response.text();
		throw new Error(errorText);
	}

	dispatch({
		type: LOGIN_SUCCESS,
		payload: { login }
	});

	} catch (e) {
	dispatch({
		type: LOGIN_ERROR,
		payload: e.message
	});
	}
};

/*
 Регистрация
*/
export const register = (login, passwd) => async dispatch => {
	try {
		const response = await fetch('/lab4/api/main/register', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			credentials: 'include',
			body: JSON.stringify({ login, passwd })
	});

    if (!response.ok) {
		const errorText = await response.text();
		throw new Error(errorText);
    }

    dispatch({
		type: LOGIN_SUCCESS,
		payload: { login }
    });

  } catch (e) {
	dispatch({
		type: REGISTER_ERROR,
		payload: e.message
    });
  }
};

/*
 Проверка текущей сессии
*/
export const fetchMe = () => async dispatch => {
  try {
    const response = await fetch('/lab4/api/main/me', {
      method: 'GET',
      credentials: 'include'
    });

    if (!response.ok) {
		dispatch({
		  type: LOGIN_ERROR,
		  payload: null
		});
		
		return;
	}

    const data = await response.json();

    dispatch({
      type: SET_USER,
      payload: data
    });

  } catch (e) {
		dispatch({
			  type: LOGIN_ERROR,
			  payload: null
			});
  }
};

/*
 Логаут
*/
export const logout = () => async dispatch => {
  await fetch('/lab4/api/main/logout', {
    method: 'POST',
    credentials: 'include'
  });

  dispatch({ type: LOGOUT });
};

/*
 Чистка ошибок
*/
export const clearLoginError = () => ({
	type: CLEAR_LOGIN_ERROR
});

export const clearRegisterError = () => ({
	type: CLEAR_REGISTER_ERROR 
});