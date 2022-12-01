import { useEffect, useState } from 'react'
import Header from '../components/header';
import FloatingButton from '../components/floating-button';
import { useRouter } from 'next/router';
import { postData } from '../utils/request';
import { extractData } from '../utils/storage';

export default function Schedule() {
  const router = useRouter();

  const [schedules, setSchedules] = useState([]);

  const fetchData = async (data) => {
    const response = await postData(process.env.NEXT_PUBLIC_API + '/teams/schedule', data);
    setSchedules(response);
  }

  useEffect(() => {
    if (!router.isReady) return;
    const chromosome = router.query['chromosome'].split(',');
    const teams = extractData('teams');
    const data = {
      teams,
      individual: {
        chromosome
      }
    }
    fetchData(data);
  }, [router.isReady]);

  const body = () => {
    const fixture = [];
    for (let i = 0; i < schedules.length; i++) {
      const matchRound = schedules[i]['matches'];
      const element = [];
      for (let j = 0; j < matchRound.length; j++) {
        const match = matchRound[j];
        const homeTeam = match['homeTeam'];
        const awayTeam = match['awayTeam'];
        const node = (
          <div className='column is-half has-text-centered' key={j}>
            <div className='box is-flex is-align-items-center is-justify-content-center' style={{height: '100%'}}>
              <div className='' style={{flex: '1 0 0'}}>
                <img src={homeTeam['logo']} />
                <div className='is-size-6'>{homeTeam['name']}</div>
              </div>
              <div className='' style={{flex: '1 0 0'}}>
                <p className='is-size-6'>19:00</p>
                <p className='is-size-6'>13/11/2022</p>
              </div>
              <div className='' style={{flex: '1 0 0'}}>
                <img src={awayTeam['logo']} />
                <div className='is-size-6'>{awayTeam['name']}</div>
              </div>
            </div>
          </div>
        );
        element.push(node);
      }
      fixture.push((
        <div key={i} className='pt-3'>
          <h3 className='is-size-5 has-text-weight-medium'>Vòng {i + 1}</h3>
          <div className='columns is-multiline mt-0'>
            {element}
          </div>
        </div>
      ));
    }
    return fixture;
  }

  const onSubmit = () => {
    window.location = '/';
  }

  if (schedules.length == 0) return <></>

  return (
    <>
      <Header title='Lịch thi đấu Ngoại Hạng Anh:' />
      <section className='container' style={{ margin: '200px 250px 0' }}>
        {body()}
      </section>
      <FloatingButton title='Về trang chủ' onClick={onSubmit} />
    </>
  );
}
