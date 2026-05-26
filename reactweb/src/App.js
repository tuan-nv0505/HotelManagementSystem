import { BrowserRouter, Route, Routes } from "react-router-dom";
import { useReducer } from "react";
import cookies from 'react-cookies';
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./screens/Home/Home";
import { MyUserContext } from "./configs/Contexts";
import myUserReducer from "./reducers/MyUserReducer";
import RoomType from "./screens/Booking/RoomType";
import Room from "./screens/Booking/Room";
import { GoogleOAuthProvider } from "@react-oauth/google";
import Profile from "./screens/User/Profile";


const App = () => {
  const [user, dispatch] = useReducer(myUserReducer, cookies.load("user") || null);

  return (
    <GoogleOAuthProvider clientId={process.env.REACT_APP_GOOGLE_CLIENT_ID}>
      <MyUserContext.Provider value={[user, dispatch]}>
        <BrowserRouter>
          <Header />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/room-types" element={<RoomType />} >
              <Route path=":id/rooms" element={<Room />} >
                <Route path=":roomId/services" />
              </Route>
            </Route>
            <Route path="/profile" element={<Profile />}></Route>
          </Routes>
          <Footer />
        </BrowserRouter>
      </MyUserContext.Provider>
    </GoogleOAuthProvider>
  );
}


export default App;