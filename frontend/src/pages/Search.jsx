import { useState } from 'react';
import api from '../api/axios';
import TrackCard from '../components/TrackCard';

export default function Search() {
  const [query, setQuery] = useState('');
  const [tracks, setTracks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [searched, setSearched] = useState(false);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;
    setLoading(true);
    setSearched(true);
    try {
      const res = await api.get('/music/search', { params: { track: query } });
      const list = res.data?.results?.trackmatches?.track || [];
      setTracks(Array.isArray(list) ? list : [list]);
    } catch {
      setTracks([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page">
      <h2 className="section-title">🔍 음악 검색</h2>
      <p className="section-subtitle">곡명이나 아티스트를 검색하면 AI가 분위기를 분석해드려요</p>

      <form className="search-bar" onSubmit={handleSearch}>
        <input
          className="search-input"
          type="text"
          placeholder="곡명 또는 아티스트 검색..."
          value={query}
          onChange={e => setQuery(e.target.value)}
        />
        <button type="submit" className="btn btn-primary">검색</button>
      </form>

      {loading && (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: 60 }}>
          <div className="spinner" />
        </div>
      )}

      {!loading && searched && tracks.length === 0 && (
        <p style={{ color: '#888', textAlign: 'center', marginTop: 60 }}>
          검색 결과가 없어요.
        </p>
      )}

      {!loading && tracks.length > 0 && (
        <div className="track-grid">
          {tracks.map((track, idx) => (
            <TrackCard key={idx} track={track} />
          ))}
        </div>
      )}
    </div>
  );
}
