import axios from "axios";
import cookies from 'react-cookies'

export const endpoints = {
    'login': '/auth/login',
    'register': '/auth/register',
    'profile': '/secure/profile',
    'roomTypes': '/room-types',
    'availableRooms': '/rooms/available',
    'google': '/auth/google',
    'facebook': '/auth/facebook',
    'update-profile': '/secure/update/profile'
}

export const authApis = () => {
    return axios.create({
        baseURL: `${process.env.REACT_APP_API_BASE_URL}/api/`,
        headers: {
            'Authorization': `Bearer ${cookies.load('token')}`
        }
    })
}


export default axios.create({
    baseURL: `${process.env.REACT_APP_API_BASE_URL}/api/`
})