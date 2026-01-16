import {
  SET_X,
  SET_Y,
  SET_R,
  LOAD_RESULTS,
  ADD_RESULT,
  CLEAR_RESULTS
} from './appTypes';

const initialState = {
  x: null,
  y: '',
  r: 0,
  results: []
};

export default function appReducer(state = initialState, action) {
  switch (action.type) {

    case SET_X:
		return { ...state, x: action.payload };

    case SET_Y:
		return { ...state, y: action.payload };

    case SET_R:
		return { ...state, r: action.payload };

    case LOAD_RESULTS:
		return { ...state, results: Array.isArray(action.payload) ? action.payload : [] };

    case ADD_RESULT:
		return { ...state, results: [action.payload, ...state.results] };

    case CLEAR_RESULTS:
		return initialState;

    default:
		return state;
  }
}
