import { useRouter } from 'next/router';
import { useEffect, useState } from 'react'
import Header from '../components/header';
import { getData, postData } from '../utils/request';
import { clearStore, storeData } from '../utils/storage';
import { toastError } from '../utils/toast';
import FloatingButton from '../components/floating-button';
import ProgressBar from '../components/progress';

export default function Home() {
  const router = useRouter();

  const [teams, setTeams] = useState([])
  const [hasProgress, setProgess] = useState(false);

  let data = Array(teams?.length).fill(null);

  const fetchData = async () => {
    const response = await getData(process.env.NEXT_PUBLIC_API + '/teams');
    setTeams(response);
  }

  useEffect(() => {
    fetchData();
    clearStore();
  }, []);

  const handleCheck = (e, team, i) => {
    if (e.target.checked) {
      data[i] = team;
    } else {
      data[i] = null;
    }
  }

  const onSubmit = async () => {
    const _data = data.filter(e => e);
    if (_data.length < 2) {
      toastError('Số đội không hợp lệ');
      return
    }
    setProgess(true);
    const response = await postData(process.env.NEXT_PUBLIC_API + '/ga/init', _data);
    storeData('teams', _data);
    storeData('population', response);
    setProgess(false);
    router.push('/init');
  }

  const body = () => {
    const element = [];
    for (let i = 0; i < teams?.length; i++) {
      const team = teams[i];
      const name = team['name'];
      const logo = team['logo'];
      const node = (
        <div className='column is-half' key={i}>
          <div className='box'>
            <div className='level'>
              <span className='is-size-5 ml-2'>{i + 1}</span>
              <div className='is-flex is-align-items-center is-flex-grow-1'>
                <div className='px-3'>
                  <img src={logo} />
                </div>
                <span className='is-size-5'>{name}</span>
              </div>
              <label className='b-checkbox checkbox is-medium'>
                <input type="checkbox" onClick={(e) => handleCheck(e, team, i)} />
                <span className="check is-info"></span>
              </label>
            </div>
        </div>
        </div>
      );
      element.push(node);
    }
    return (
      <div className='columns is-multiline'>
        {element}
      </div>
    );
  }

  return (
    <>
      <Header title='Chọn đội bóng để khởi tạo quần thể:' />
      <section className='container' style={{margin: '210px 250px 0'}}>
        {body()}
      </section>
      <ProgressBar isActive={hasProgress} />
      <FloatingButton title='Tiếp tục' onClick={onSubmit} />
    </>
  );
}
