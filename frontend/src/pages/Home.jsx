import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import TrackCard from '../components/TrackCard';

export default function Home() {
  const [tracks, setTracks] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    api.get('/music/top')
      .then(res => {
        const list = res.data?.tracks?.track || [];
        setTracks(list);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="page">
      <div className="hero-section">
        <h1 className="hero-title">AURA</h1>
        <p className="hero-sub">AI가 분석하는 음악의 감성과 분위기</p>
        <button className="btn btn-primary" onClick={() => navigate('/search')}>
          음악 검색하기
        </button>
      </div>

      <h2 className="section-title">🔥 글로벌 차트 Top 20</h2>
      <p className="section-subtitle">Last.fm 기준 전 세계에서 가장 많이 들리는 곡</p>

      {loading ? (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: 60 }}>
          <div className="spinner" />
        </div>
      ) : (
        <div className="track-grid">
          {tracks.map((track, idx) => (
            <TrackCard key={idx} track={track} />
          ))}
        </div>
      )}
    </div>
  );
}
