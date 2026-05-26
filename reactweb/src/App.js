import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useReducer } from "react";
import cookies from 'react-cookies';
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./screens/Home/Home";
import { MyUserContext } from "./configs/Contexts";
import myUserReducer from "./reducers/MyUserReducer";
import { GoogleOAuthProvider } from "@react-oauth/google";

const App = () => {
  const [user, dispatch] = useReducer(myUserReducer, cookies.load("user") || null);

  return (
    <GoogleOAuthProvider clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}>
      <MyUserContext.Provider value={[user, dispatch]}>
        <BrowserRouter>
          <Header />
          <Routes>
            <Route path="/" element={<Home />} />
          </Routes>
          <Footer />
        </BrowserRouter>
      </MyUserContext.Provider>
    </GoogleOAuthProvider>
  );
}

export default App;