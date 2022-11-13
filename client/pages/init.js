import { useEffect, useState } from 'react'
import Header from '../components/header';
import { extractData, storeData } from '../utils/storage';
import FloatingButton from '../components/floating-button';
import { useRouter } from 'next/router';
import { postData } from '../utils/request';

export default function Init() {
  const router = useRouter();

  const [individuals, setIndividuals] = useState([]);

  useEffect(() => {
    if (!router.isReady) return;
    const index = router.query['index'];
    if (index) {
      const generations = extractData('generations');
      const population = generations[index - 1];
      setIndividuals(population?.['individuals']);
    } else {
      const population = extractData('population');
      setIndividuals(population?.['individuals']);
    }
  }, [router.isReady]);

  const onViewDetail = (chromosome) => {
    router.push(`/schedule?chromosome=${chromosome}`);
  }

  const onSubmit = async () => {
    const population = extractData('population');
    const teams = extractData('teams');
    const data = {
      teams,
      population
    }
    const response = await postData(process.env.NEXT_PUBLIC_API + '/ga/exec', data);
    storeData('generations', response);
    router.push('/ga');
  }

  if (individuals.length == 0) return <></>;

  return (
    <>
      <Header title='Quần thể sau khi khởi tạo:' />
      <section className='container' style={{ margin: '200px 250px 0' }}>
        <div>
          <table className="table is-fullwidth is-striped has-sticky-header">
          <thead>
            <tr>
              <th>STT</th>
              <th>Cá thể</th>
              <th>Độ thích nghi</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {individuals.map((e, i) => {
              const chromosome = e['chromosome'].join(',');
              const fitness = e['fitness'].toPrecision(2);
              return (<tr key={i}>
                <td>{i + 1}</td>
                <td>{chromosome}</td>
                <td>{fitness}</td>
                <td><a onClick={() => onViewDetail(chromosome)}>Xem chi tiết</a></td>
              </tr>)
            })}
          </tbody>
        </table>
        </div>
      </section>
      <FloatingButton title='Tiếp tục' onClick={onSubmit} />
    </>
  );
}
