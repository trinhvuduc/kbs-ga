const ProgressBar = ({isActive}) => {
  return (
    <div className={`modal${isActive ? ' is-active' : ''}`}>
        <div className="modal-background"></div>
        <div className="modal-content">
          <progress className="progress is-small is-info mx-auto" max="100" style={{width: '50%'}} />
        </div>
      </div>
  );
}

export default ProgressBar;