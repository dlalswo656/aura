import { useNavigate } from 'react-router-dom';

export default function TrackCard({ track }) {
  const navigate = useNavigate();
  const image = track.image?.find(i => i.size === 'large')?.['#text'];

  const handleClick = () => {
    navigate('/track', { state: { track } });
  };

  return (
    <div className="track-card" onClick={handleClick}>
      {image ? (
        <img src={image} alt={track.name} />
      ) : (
        <div className="track-card-img-placeholder">🎵</div>
      )}
      <div className="track-card-info">
        <div className="track-card-title">{track.name}</div>
        <div className="track-card-artist">
          {track.artist?.name || track.artist}
        </div>
      </div>
    </div>
  );
}
