import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo">AURA</Link>
      <div className="navbar-links">
        <Link to="/">홈</Link>
        <Link to="/search">검색</Link>
        {user ? (
          <>
            <span style={{ color: '#a855f7', fontSize: '0.9rem' }}>
              {user.username}
            </span>
            <button className="btn btn-outline" onClick={handleLogout}>
              로그아웃
            </button>
          </>
        ) : (
          <>
            <Link to="/login">로그인</Link>
            <Link to="/signup">
              <button className="btn btn-primary">회원가입</button>
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}
