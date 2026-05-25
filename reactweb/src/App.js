import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useReducer } from "react";
import cookies from 'react-cookies';
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./screens/Home/Home";
import { MyUserContext } from "./configs/Contexts"; 
import myUserReducer from "./reducers/MyUserReducer"; 

const App = () => {
  const [user, dispatch] = useReducer(myUserReducer, cookies.load("user") || null);

  return (
    <MyUserContext.Provider value={[user, dispatch]}>
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/" element={<Home />} />
        </Routes>
        <Footer />
      </BrowserRouter>
    </MyUserContext.Provider>
  );
}

export default App;