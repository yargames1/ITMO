import {
  SET_X,
  SET_Y,
  SET_R,
  LOAD_RESULTS,
  ADD_RESULT,
  CLEAR_RESULTS
} from './appTypes';


export const setX = value => ({
	type: SET_X,
	payload: value
});

export const setY = value => ({
	type: SET_Y,
	payload: value
});

export const setR = value => ({
	type: SET_R,
	payload: value
});

export const loadResults = () => async dispatch => {
	try {
		const response = await fetch('/lab4/api/points', {
		method: 'GET',
		credentials: 'include'
	});

	if (!response.ok) {
		throw new Error('Failed to load results');
	}

	const data = await response.json();
	const results = Array.isArray(data) ? data : [];

	dispatch({
		type: LOAD_RESULTS,
		payload: results
	});
	} catch (error) {
		console.error('Error loading results:', error);
		dispatch({
		type: LOAD_RESULTS,
		payload: []
	});
	}
};

export const addResult = result => ({
	type: ADD_RESULT,
	payload: result
});

export const clearResults = () => async dispatch => {
  try {
	const response = await fetch('/lab4/api/points', {
		method: 'DELETE',
		credentials: 'include'
	});

	if (!response.ok) {
		throw new Error(`${response.status}`);
	}

	dispatch({
		type: CLEAR_RESULTS
	});
	} catch (error) {
		console.error('Error deleting results:', error);
	}
};