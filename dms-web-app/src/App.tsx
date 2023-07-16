import React, { useState } from 'react';
import { BrowserRouter, Routes, Route, Link, Navigate } from "react-router-dom";
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Profile from './pages/Profile';

const App: React.FC = () => {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));

    const handleLogout = () => {
        // Clear the token on logout
        setToken(null);
        localStorage.removeItem('token');
    };

    const isAuthenticated = !!token;

    return (
        <BrowserRouter>
        <div>
            <nav>
                <ul>
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                    {!isAuthenticated && (
                        <>
                            <li>
                                <Link to="/login">Login</Link>
                            </li>
                            <li>
                                <Link to="/register">Register</Link>
                            </li>
                        </>
                    )}
                    {isAuthenticated && (
                        <>
                            <li>
                                <Link to="/profile">Profile</Link>
                            </li>
                            <li>
                                <button onClick={handleLogout}>Logout</button>
                            </li>
                        </>
                    )}
                </ul>
            </nav>

                <Routes>
                    <Route path="/" element={<Login />}>
                        {!isAuthenticated && <Route path="/login" element={<Login />} />}
                        {!isAuthenticated && <Route path="/register" element={<Register />} />}
                        {isAuthenticated && <Route path="/profile" element={<Profile />} />}
                        {isAuthenticated && <Route path="/home" element={<Home />} />}
                        {!isAuthenticated && <Route path="/*" element={<Navigate to="/login" replace />} />}
                    </Route>
                </Routes>

        </div>
        </BrowserRouter>
    );
};

export default App;
