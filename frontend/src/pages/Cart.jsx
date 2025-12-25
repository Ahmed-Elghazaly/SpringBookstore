import {useEffect, useState} from 'react';
import {cartApi} from '../api/client';
import toast from 'react-hot-toast';
import {CreditCard, Trash2} from 'lucide-react';
import {useNavigate} from 'react-router-dom';

export default function Cart() {
    const [cart, setCart] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showCheckout, setShowCheckout] = useState(false);
    const [cardInfo, setCardInfo] = useState({ number: '', expiry: '' });
    const customerId = localStorage.getItem('customerId');
    const navigate = useNavigate();

    const fetchCart = async () => {
        try {
            const res = await cartApi.get(customerId);
            setCart(res.data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchCart();
    }, []);

    const handleUpdate = async (isbn, qty) => {
        if (qty < 1) return;
        await cartApi.update(customerId, isbn, qty);
        fetchCart();
    };

    const handleRemove = async (isbn) => {
        await cartApi.remove(customerId, isbn);
        toast.success("Item removed");
        fetchCart();
    };

    const handleCheckout = async () => {
        if (!cardInfo.number || !cardInfo.expiry) {
            toast.error("Please enter credit card details");
            return;
        }

        try {
            const res = await cartApi.checkout(customerId, {
                creditCardNumber: cardInfo.number,
                expiryDate: cardInfo.expiry
            });
            toast.success(`Order Placed! ID: ${res.data.orderId}`);
            setCardInfo({ number: '', expiry: '' }); // Clear form
            navigate('/orders');
        } catch (err) {
            const errorMsg = err.response?.data?.error || "Checkout failed. Check stock or balance.";
            toast.error(errorMsg);
        }
    };

    if (loading) return <div className="p-10 text-center">Loading cart...</div>;
    if (!cart || cart.items.length === 0) {
        return <div className="p-10 text-center text-gray-500">Your cart is empty.</div>;
    }

    return (
        <div className="max-w-4xl mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-8">Shopping Cart</h1>

            <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                {cart.items.map((item) => (
                    <div key={item.isbn} className="flex items-center justify-between p-6 border-b border-gray-100 last:border-0">
                        <div>
                            <p className="text-xs text-gray-400 font-mono">ISBN: {item.isbn}</p>
                            <h3 className="font-bold text-lg text-gray-800">{item.title}</h3>
                            <p className="text-sm text-gray-500">${item.price.toFixed(2)} each</p>
                        </div>
                        <div className="flex items-center gap-6">
                            <div className="flex items-center border rounded-lg">
                                <button
                                    onClick={() => handleUpdate(item.isbn, item.quantity - 1)}
                                    className="px-3 py-1 hover:bg-gray-100"
                                >
                                    -
                                </button>
                                <span className="px-3 font-mono">{item.quantity}</span>
                                <button
                                    onClick={() => handleUpdate(item.isbn, item.quantity + 1)}
                                    className="px-3 py-1 hover:bg-gray-100"
                                >
                                    +
                                </button>
                            </div>
                            <div className="text-right min-w-[100px]">
                                <div className="font-bold text-gray-900">${item.subtotal.toFixed(2)}</div>
                            </div>
                            <button
                                onClick={() => handleRemove(item.isbn)}
                                className="text-red-400 hover:text-red-600"
                            >
                                <Trash2 size={20} />
                            </button>
                        </div>
                    </div>
                ))}

                <div className="p-6 bg-gray-50 border-t border-gray-200">
                    <div className="flex justify-between items-center">
                        <span className="text-xl font-bold text-gray-700">Total:</span>
                        <span className="text-2xl font-bold text-green-600">${cart.totalPrice.toFixed(2)}</span>
                    </div>
                </div>
            </div>

            {/* Credit Card Payment Section */}
            {showCheckout && (
                <div className="bg-white p-6 rounded-xl mb-4 mt-6 border-2 border-brand-200">
                    <h3 className="font-bold text-lg mb-4 text-gray-800">Payment Information</h3>
                    <div className="space-y-3">
                        <input
                            type="text"
                            placeholder="Card Number (min 13 digits)"
                            className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                            value={cardInfo.number}
                            onChange={(e) => setCardInfo({...cardInfo, number: e.target.value})}
                            maxLength="16"
                        />
                        <input
                            type="date"
                            placeholder="Expiry Date"
                            className="w-full p-3 border rounded-lg focus:ring-2 focus:ring-brand-500 outline-none"
                            value={cardInfo.expiry}
                            onChange={(e) => setCardInfo({...cardInfo, expiry: e.target.value})}
                            min={new Date().toISOString().split('T')[0]}
                        />
                    </div>
                </div>
            )}

            <div className="mt-8 flex justify-end">
                <button
                    onClick={() => showCheckout ? handleCheckout() : setShowCheckout(true)}
                    className="bg-green-600 text-white px-8 py-4 rounded-xl font-bold text-lg hover:bg-green-700 flex items-center gap-3 shadow-lg shadow-green-200 transition-all"
                >
                    <CreditCard /> {showCheckout ? 'Complete Purchase' : 'Proceed to Payment'}
                </button>
            </div>
        </div>
    );
}