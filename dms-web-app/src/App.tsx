import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Home from './pages/Home';
import Login from './pages/Login';
import Profile from './pages/Profile';

const App: React.FC = () => {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

    const handleLogin = (token: string) => {
        console.log(token,)
    }

    const handleLogout = () => {
        // Clear the token on logout
        setToken(null);
        localStorage.removeItem('token');
    };

    const isAuthenticated = !!token;

    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login handleLogin={handleLogin} />}>
                    {!isAuthenticated && <Route path="/login" element={<Login handleLogin={handleLogin} />} />}
                    {isAuthenticated && <Route path="/profile" element={<Profile />} />}
                    {isAuthenticated && <Route path="/home" element={<Home />} />}
                    {!isAuthenticated && <Route path="/*" element={<Navigate to="/login" replace />} />}
                </Route>
            </Routes>
        </BrowserRouter>
    );
};

export default App;
