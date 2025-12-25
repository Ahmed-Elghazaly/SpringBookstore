import {useEffect, useState} from 'react';
import {publisherOrderApi} from '../api/client';
import toast from 'react-hot-toast';
import {CheckCircle, Package, RefreshCw} from 'lucide-react';

export default function AdminPublisherOrders() {
    // State for the list of pending orders fetched from the backend
    const [orders, setOrders] = useState([]);
    // Loading state for the initial data fetch
    const [loading, setLoading] = useState(true);
    // Tracks which order is currently being confirmed (for showing loading spinner on that button)
    const [confirming, setConfirming] = useState(null);

    // Load pending orders when the component mounts
    useEffect(() => {
        loadOrders();
    }, []);

    // Fetches all pending publisher orders from the backend
    const loadOrders = async () => {
        setLoading(true);
        try {
            const res = await publisherOrderApi.getPending();
            setOrders(res.data);
        } catch (err) {
            console.error(err);
            toast.error('Failed to load publisher orders');
        } finally {
            setLoading(false);
        }
    };

    // Handles the confirmation of a publisher order
    // When confirmed, the backend will:
    // 1. Change order status from 'Pending' to 'Confirmed'
    // 2. Increase the stock quantity of all books in the order
    const handleConfirm = async (orderId) => {
        setConfirming(orderId);
        try {
            await publisherOrderApi.confirm(orderId);
            toast.success('Order confirmed! Stock has been updated.');
            // Reload the list to reflect the changes (the confirmed order will disappear)
            loadOrders();
        } catch (err) {
            console.error(err);
            toast.error(err.response?.data?.message || 'Failed to confirm order');
        } finally {
            setConfirming(null);
        }
    };

    // The backend returns a flat list where each row represents one book in an order.
    // We need to group these rows by orderId so we can display each order as a card
    // with all its books listed inside.
    const groupedOrders = orders.reduce((acc, row) => {
        const key = row.orderId;
        if (!acc[key]) {
            // First time seeing this order, create the base object
            acc[key] = {
                orderId: row.orderId,
                orderDate: row.orderDate,
                status: row.status,
                books: []
            };
        }
        // Add this book to the order's books array
        acc[key].books.push({
            isbn: row.isbn,
            title: row.bookTitle,
            quantity: row.quantity,
            publisherName: row.publisherName
        });
        return acc;
    }, {});

    // Convert the grouped object to an array for easier mapping in the render
    const orderList = Object.values(groupedOrders);

    // Show loading state while data is being fetched
    if (loading) {
        return (
            <div className="max-w-6xl mx-auto px-4 py-8">
                <div className="text-center py-20 text-gray-500">Loading publisher orders...</div>
            </div>
        );
    }

    return (
        <div className="max-w-6xl mx-auto px-4 py-8">
            {/* Page Header with Refresh Button */}
            <div className="flex justify-between items-center mb-8">
                <div>
                    <h1 className="text-3xl font-bold text-gray-900">Publisher Orders</h1>
                    <p className="text-gray-500 mt-1">
                        Confirm pending orders to add stock to inventory
                    </p>
                </div>
                <button
                    onClick={loadOrders}
                    className="bg-gray-100 text-gray-600 px-4 py-2 rounded-lg font-medium hover:bg-gray-200 flex items-center gap-2"
                >
                    <RefreshCw size={18}/> Refresh
                </button>
            </div>

            {/* Empty State - shown when there are no pending orders */}
            {orderList.length === 0 && (
                <div className="bg-white rounded-xl shadow-sm border p-12 text-center">
                    <Package size={48} className="mx-auto text-gray-300 mb-4"/>
                    <h2 className="text-xl font-bold text-gray-600 mb-2">No Pending Orders</h2>
                    <p className="text-gray-400">
                        Publisher orders are automatically created when book stock drops below the threshold quantity.
                    </p>
                    <p className="text-gray-400 mt-2">
                        Try purchasing some books to trigger the auto-ordering mechanism!
                    </p>
                </div>
            )}

            {/* Orders List - each order is displayed as a card */}
            <div className="space-y-6">
                {orderList.map((order) => (
                    <div key={order.orderId} className="bg-white rounded-xl shadow-sm border overflow-hidden">
                        {/* Order Header - shows order ID, date, and status badge */}
                        <div className="bg-gray-50 p-4 border-b flex justify-between items-center">
                            <div>
                                <span className="text-sm text-gray-500">Order ID:</span>
                                <span className="ml-2 font-mono font-bold text-brand-600">#{order.orderId}</span>
                                <span className="mx-3 text-gray-300">|</span>
                                <span className="text-sm text-gray-500">Date:</span>
                                <span className="ml-2 font-medium">{order.orderDate}</span>
                            </div>
                            <span className="px-3 py-1 bg-yellow-100 text-yellow-700 rounded-full text-sm font-medium">
                                {order.status}
                            </span>
                        </div>

                        {/* Order Books Table - lists all books in this order */}
                        <div className="p-4">
                            <table className="w-full">
                                <thead>
                                    <tr className="text-left text-sm text-gray-500">
                                        <th className="pb-2">Book</th>
                                        <th className="pb-2">Publisher</th>
                                        <th className="pb-2 text-right">Quantity to Add</th>
                                    </tr>
                                </thead>
                                <tbody className="divide-y">
                                    {order.books.map((book, idx) => (
                                        <tr key={idx}>
                                            <td className="py-3">
                                                <div className="font-bold text-gray-900">{book.title}</div>
                                                <div className="text-xs text-gray-400 font-mono">{book.isbn}</div>
                                            </td>
                                            <td className="py-3 text-gray-600">{book.publisherName}</td>
                                            <td className="py-3 text-right">
                                                {/* Quantity shown with + prefix to indicate it will be added to stock */}
                                                <span className="bg-brand-100 text-brand-700 px-3 py-1 rounded-full font-bold">
                                                    +{book.quantity}
                                                </span>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>

                        {/* Order Actions - Confirm button */}
                        <div className="bg-gray-50 p-4 border-t flex justify-end">
                            <button
                                onClick={() => handleConfirm(order.orderId)}
                                disabled={confirming === order.orderId}
                                className="bg-green-600 text-white px-6 py-3 rounded-lg font-bold hover:bg-green-700 disabled:bg-gray-400 disabled:cursor-not-allowed flex items-center gap-2"
                            >
                                {confirming === order.orderId ? (
                                    <>
                                        {/* Show spinner while confirming */}
                                        <RefreshCw size={18} className="animate-spin"/> Confirming...
                                    </>
                                ) : (
                                    <>
                                        <CheckCircle size={18}/> Confirm Order
                                    </>
                                )}
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
