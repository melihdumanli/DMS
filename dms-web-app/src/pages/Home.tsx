import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import "react-toastify/dist/ReactToastify.css";
import axios from 'axios';
import '../assets/styles/home.scss';

const addDocumentUrl = 'http://localhost:8080/api/v1/document';
const deleteDocumentUrl = 'http://localhost:8080/api/v1/document';
const fetchDocumentsUrl = 'http://localhost:8080/api/v1/document/findall';
const getDocumentUrl = 'http://localhost:8080/api/v1/document';
const updateDocumentUrl = 'http://localhost:8080/api/v1/document';

const Home: React.FC = () => {
    const [documents, setDocuments] = useState([]);
    const [token, setToken] = useState<string | null>(
        localStorage.getItem('access_token')
    );
    const navigate = useNavigate();

    useEffect(() => {
        !token && navigate('/login', { replace: true });
    }, [token]);

    useEffect(() => {
        fetchDocuments()
    }, [token]);

    const notify = (message: string) => {
        toast.error(message, {
            position: toast.POSITION.TOP_RIGHT
        });
    };

    const fetchDocuments = () => {
        axios
            .get(fetchDocumentsUrl, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            .then((response) => setDocuments(response.data))
            .catch((error) => {
                error.response.status === 403
                    ? navigate('/login', { replace: true })
                    : notify('No Documents Found!')
            });
    }

    const handleLogout = () => {
        setToken(null);
        localStorage.removeItem('access_token');
        navigate('/login');
    };

    const handleAddDocument = (file: File | undefined) => {
        if (!file) {
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        axios
            .post(addDocumentUrl, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': file.type,
                },
            })
            .then((response) => {
                fetchDocuments()
            })
            .catch((error) => {
                error.response.status === 403
                    ? navigate('/login', { replace: true })
                    : notify('File Upload Interrupted!')
            });
    };


    const handleUpdateDocument = (file: File | undefined, documentId: number) => {
        if (!file) {
            console.error('Dosya seçilmedi.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        axios
            .put(`${updateDocumentUrl}?id=${documentId}`, formData, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': file.type,
                },
            })
            .then((response) => {
                fetchDocuments()
            })
            .catch((error) => {
                error.response.status === 403
                    ? navigate('/login', { replace: true })
                    : notify('File Upload Interrupted!')
            });
    };

    const handleDeleteDocument = (documentId: number) => {
        axios
            .delete(`${deleteDocumentUrl}?id=${documentId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            .then((response) => {
                fetchDocuments()
            })
            .catch((error) => {
                error.response.status === 403
                    ? navigate('/login', { replace: true })
                    : notify('File Could Not Be Deleted!')
            });
    };

    const handleDownloadDocument = (documentId: number, fileName: string) => {
        axios
            .get(`${getDocumentUrl}?id=${documentId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
                responseType: 'blob',
            })
            .then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            })
            .catch((error) => {
                error.response.status === 403
                    ? navigate('/login', { replace: true })
                    : notify('File Could Not Be Downloaded!')
            });
    };

    const formatFileSize = (bytes: number) => {
        const megabytes = bytes / (1024 * 1024);
        return `${megabytes.toFixed(2)} MB`;
    }

    const formatDate = (dateString: string) => {
        const options = {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: 'numeric',
            minute: 'numeric',
            hour12: false,
        } as Intl.DateTimeFormatOptions;

        const date = new Date(dateString);
        return date.toLocaleDateString('tr-TR', options);
    }

    return (
        <div className="home-container">
            <div className="header">
                <h1>Home Page</h1>
                <button onClick={handleLogout}>Logout</button>
            </div>
            <input
                type="file"
                id="file-input"
                className="choose-file-btn"
                onChange={(e) => handleAddDocument(e.target.files?.[0])}
            />
            <table className="document-table">
                <thead>
                    <tr>
                        <th>File Name</th>
                        <th>Create Date</th>
                        <th>Size</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {documents.map((document: any) => (
                        <tr key={document.id}>
                            <td>{document.fileName}</td>
                            <td>{formatDate(document.createDate)}</td>
                            <td>{formatFileSize(document.fileSize)}</td>
                            <td>
                                <button
                                    className="download-btn"
                                    onClick={() => handleDownloadDocument(document.id, document.fileName)}
                                >
                                    Download
                                </button>
                                <button
                                    className="delete-btn"
                                    onClick={() => handleDeleteDocument(document.id)}
                                >
                                    Delete
                                </button>
                                <input
                                    type="file"
                                    onChange={(e) =>
                                        handleUpdateDocument(e.target.files?.[0], document.id)
                                    }
                                />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <ToastContainer />
        </div>
    );
};

export default Home;
