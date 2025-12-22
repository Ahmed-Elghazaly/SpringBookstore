import { Link, useNavigate } from 'react-router-dom';
import { ShoppingCart, User, LogOut, BarChart3 } from 'lucide-react';

export default function Navbar() {
    const navigate = useNavigate();
    const customerId = localStorage.getItem('customerId');

    const handleLogout = () => {
        localStorage.removeItem('customerId');
        navigate('/login');
    };

    return (
        <nav className="bg-white border-b border-gray-200 sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex items-center cursor-pointer" onClick={() => navigate('/')}>
                        <span className="text-2xl font-bold text-brand-600">GoldenBooks</span>
                    </div>

                    <div className="flex items-center space-x-6">
                        {customerId ? (
                            <>
                                <Link to="/" className="text-gray-600 hover:text-brand-600 font-medium">Browse</Link>
                                <Link to="/orders" className="text-gray-600 hover:text-brand-600 font-medium">Orders</Link>
                                <Link to="/admin" className="text-gray-600 hover:text-brand-600 font-medium flex items-center gap-1">
                                    <BarChart3 size={18} /> Admin
                                </Link>

                                <Link to="/cart" className="relative text-gray-600 hover:text-brand-600">
                                    <ShoppingCart size={24} />
                                    {/* Badge could go here if we tracked count globally */}
                                </Link>

                                <div className="h-6 w-px bg-gray-300 mx-2"></div>

                                <div className="flex items-center gap-3">
                                    <span className="text-sm font-semibold text-gray-700">User #{customerId}</span>
                                    <button onClick={handleLogout} className="text-red-500 hover:bg-red-50 p-2 rounded-full">
                                        <LogOut size={20} />
                                    </button>
                                </div>
                            </>
                        ) : (
                            <Link to="/login" className="text-brand-600 font-bold">Login</Link>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
}