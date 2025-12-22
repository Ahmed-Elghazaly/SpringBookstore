import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Home from './pages/Home';
import Cart from './pages/Cart';
import OrderHistory from './pages/OrderHistory';
import AdminDashboard from './pages/AdminDashboard';

function ProtectedRoute({ children }) {
    const customerId = localStorage.getItem('customerId');
    if (!customerId) return <Navigate to="/login" />;
    return children;
}

function App() {
    return (
        <BrowserRouter>
            <Toaster position="bottom-right" />
            <Routes>
                <Route path="/login" element={<Login />} />

                <Route path="/" element={
                    <ProtectedRoute>
                        <div className="min-h-screen bg-gray-50">
                            <Navbar />
                            <Home />
                        </div>
                    </ProtectedRoute>
                } />

                <Route path="/cart" element={
                    <ProtectedRoute>
                        <div className="min-h-screen bg-gray-50">
                            <Navbar />
                            <Cart />
                        </div>
                    </ProtectedRoute>
                } />

                <Route path="/orders" element={
                    <ProtectedRoute>
                        <div className="min-h-screen bg-gray-50">
                            <Navbar />
                            <OrderHistory />
                        </div>
                    </ProtectedRoute>
                } />

                <Route path="/admin" element={
                    <ProtectedRoute>
                        <div className="min-h-screen bg-gray-50">
                            <Navbar />
                            <AdminDashboard />
                        </div>
                    </ProtectedRoute>
                } />
            </Routes>
        </BrowserRouter>
    );
}

export default App;