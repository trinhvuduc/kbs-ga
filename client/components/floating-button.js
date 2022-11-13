const FloatingButton = ({title, onClick}) => {
	return (<div className="button is-floating is-info is-justify-content-center" onClick={onClick}>
		<span className='is-size-5 has-text-weight-medium'>{title}</span>
	</div>);
}

export default FloatingButton;