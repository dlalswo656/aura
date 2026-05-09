import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import api from '../api/axios';

export default function TrackDetail() {
  const { state } = useLocation();
  const navigate = useNavigate();
  const track = state?.track;

  const [analysis, setAnalysis] = useState('');
  const [similar, setSimilar] = useState([]);
  const [loadingAI, setLoadingAI] = useState(false);
  const [loadingSimilar, setLoadingSimilar] = useState(false);

  const image = track?.image?.find(i => i.size === 'extralarge')?.['#text']
    || track?.image?.find(i => i.size === 'large')?.['#text'];
  const artistName = track?.artist?.name || track?.artist || '';

  useEffect(() => {
    if (!track) return;

    setLoadingAI(true);
    api.get('/ai/analyze', { params: { track: track.name, artist: artistName } })
      .then(res => setAnalysis(res.data.analysis))
      .finally(() => setLoadingAI(false));

    setLoadingSimilar(true);
    api.get('/music/similar', { params: { track: track.name, artist: artistName } })
      .then(res => {
        const list = res.data?.similartracks?.track || [];
        setSimilar(Array.isArray(list) ? list.slice(0, 5) : []);
      })
      .finally(() => setLoadingSimilar(false));
  }, []);

  if (!track) {
    return (
      <div className="page" style={{ textAlign: 'center', marginTop: 60 }}>
        <p style={{ color: '#888' }}>곡 정보가 없어요.</p>
        <button className="btn btn-primary" style={{ marginTop: 20 }} onClick={() => navigate('/')}>
          홈으로
        </button>
      </div>
    );
  }

  return (
    <div className="page">
      <button
        onClick={() => navigate(-1)}
        style={{ color: '#888', background: 'none', border: 'none', cursor: 'pointer', marginBottom: 24, fontSize: '0.9rem' }}
      >
        ← 뒤로
      </button>

      {/* 트랙 정보 */}
      <div style={{ display: 'flex', gap: 32, marginBottom: 40, flexWrap: 'wrap' }}>
        {image ? (
          <img src={image} alt={track.name} style={{ width: 200, height: 200, borderRadius: 12, objectFit: 'cover' }} />
        ) : (
          <div style={{
            width: 200, height: 200, borderRadius: 12,
            background: 'linear-gradient(135deg, #1e1e2e, #2a1a3e)',
            display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '4rem'
          }}>🎵</div>
        )}
        <div style={{ flex: 1 }}>
          <h1 style={{ fontSize: '2rem', fontWeight: 800, color: '#fff', marginBottom: 8 }}>
            {track.name}
          </h1>
          <p style={{ color: '#a855f7', fontSize: '1.1rem', marginBottom: 16 }}>{artistName}</p>
          {track.listeners && (
            <p style={{ color: '#888', fontSize: '0.9rem' }}>
              리스너 {Number(track.listeners).toLocaleString()}명
            </p>
          )}
          {track.playcount && (
            <p style={{ color: '#888', fontSize: '0.9rem', marginTop: 4 }}>
              재생 {Number(track.playcount).toLocaleString()}회
            </p>
          )}
        </div>
      </div>

      {/* AI 분석 */}
      <h2 className="section-title">✨ AI 분석</h2>
      {loadingAI ? (
        <div className="analysis-loading">
          <div className="spinner" />
          <span>Gemini AI가 이 곡을 분석하고 있어요...</span>
        </div>
      ) : (
        analysis && <div className="analysis-box">{analysis}</div>
      )}

      {/* 비슷한 곡 */}
      {!loadingSimilar && similar.length > 0 && (
        <div style={{ marginTop: 40 }}>
          <h2 className="section-title">🎶 비슷한 곡 추천</h2>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 12, marginTop: 16 }}>
            {similar.map((s, idx) => {
              const sImg = s.image?.find(i => i.size === 'medium')?.['#text'];
              return (
                <div
                  key={idx}
                  onClick={() => navigate('/track', { state: { track: s } })}
                  style={{
                    display: 'flex', alignItems: 'center', gap: 16,
                    padding: '12px 16px', background: '#111120',
                    borderRadius: 10, border: '1px solid #1e1e2e', cursor: 'pointer',
                    transition: 'border-color 0.2s'
                  }}
                  onMouseEnter={e => e.currentTarget.style.borderColor = '#a855f7'}
                  onMouseLeave={e => e.currentTarget.style.borderColor = '#1e1e2e'}
                >
                  {sImg ? (
                    <img src={sImg} alt={s.name} style={{ width: 48, height: 48, borderRadius: 6, objectFit: 'cover' }} />
                  ) : (
                    <div style={{ width: 48, height: 48, borderRadius: 6, background: '#2a1a3e', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>🎵</div>
                  )}
                  <div>
                    <div style={{ fontWeight: 600, color: '#fff', fontSize: '0.9rem' }}>{s.name}</div>
                    <div style={{ color: '#888', fontSize: '0.8rem' }}>{s.artist?.name || s.artist}</div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </div>
  );
}
