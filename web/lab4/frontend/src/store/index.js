import { createStore, combineReducers, applyMiddleware } from 'redux';
import {thunk} from 'redux-thunk';

import authReducer from './auth/authReducer';
import appReducer from './app/appReducer';

const rootReducer = combineReducers({
	auth: authReducer,
	app: appReducer
});

const store = createStore(
	rootReducer,
	applyMiddleware(thunk)
);

export default store;