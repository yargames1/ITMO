import {
  LOGIN_SUCCESS,
  LOGIN_ERROR,
  CLEAR_LOGIN_ERROR,
  REGISTER_ERROR,
  CLEAR_REGISTER_ERROR,
  SET_USER,
  LOGOUT
} from './authTypes';

const initialState = {
  isAuth: false,
  loading: true,
  user: null,
  loginError: null,
  registerError: null
};

export default function authReducer(state = initialState, action) {
  switch (action.type) {

    case LOGIN_SUCCESS:
      return {
        ...state,
        isAuth: true,
		loading: false,
        user: action.payload,
        loginError: null,
		registerError: null
      };

    case SET_USER:
      return {
        ...state,
        isAuth: true,
		loading: false,
        user: action.payload,
        loginError: null,
		registerError: null
      };

    case LOGIN_ERROR:
      return {
		...state,
		isAuth: false,
		loading: false,
		user: null,
		loginError: action.payload,
		registerError: null
      };
	
	case CLEAR_LOGIN_ERROR:
		return {
			...state,
			loginError: null
		};
	  
	case REGISTER_ERROR:
		return {
			...state,
			isAuth: false,
			loading: false,
			user: null,
			loginError: null,
			registerError: action.payload
		};
	  
	case CLEAR_REGISTER_ERROR:
		return {
			...state,
			registerError: null
		};

    case LOGOUT:
      return {
		  ...initialState,
		  loading: false
	  }

    default:
		return state;
  }
}
