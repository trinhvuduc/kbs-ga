import Head from 'next/head';

const Header = ({title}) => {

  const toHomePage = () => window.location = '/';

  return (
    <>
      <Head>
        <title>Premier League</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <section className='is-fixed-top pt-3'>
        <div className='columns'>
          <div className='column has-text-centered'>
            <a href='' className='is-clickable' onClick={toHomePage}>
              <img className="m-auto" src="/premier-league-logo.png" alt="next" />
            </a>
            <h1 className="title is-3 my-3">{title}</h1>
          </div>
        </div>
      </section>
    </>
  )
}

export default Header;