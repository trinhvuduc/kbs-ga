import { useEffect, useState } from 'react'
import Header from '../components/header';
import FloatingButton from '../components/floating-button';
import { GENERATIONS } from '../utils/const';
import { extractData } from '../utils/storage';
import { useRouter } from 'next/router';

export default function GA() {
  const router = useRouter();

  const [generations, setGenerations] = useState([]);

  useEffect(() => {
    const _generations = extractData('generations');
    // setGenerations(GENERATIONS);
    setGenerations(_generations);
  }, []);

  const onViewDetail = (index) => {
    router.push(`/init?index=${index}`);
  }

  const onSubmit = () => {
    const finalPopulation = generations[generations.length - 1];
    const individuals = finalPopulation['individuals'];
    const eliteIndividual = individuals[0];
    const chromosome = eliteIndividual['chromosome'].join(',');
    router.push(`/schedule?chromosome=${chromosome}`);
  }

  return (
    <>
      <Header title='Các thế hệ mới được sinh ra từ giải thuật di truyền:' />
      <section className='container' style={{ margin: '200px 250px 0' }}>
        <div>
          <table className="table is-fullwidth is-striped has-sticky-header">
          <thead>
            <tr>
              <th>STT</th>
              <th>Quần thể ưu tú nhất</th>
              <th>Độ thích nghi</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {generations.map((e, i) => {
              const individuals = e['individuals'];
              const eliteIndividual = individuals[0];
              const chromosome = eliteIndividual['chromosome'].join(',');
              const fitness = eliteIndividual['fitness'].toPrecision(2);
              return (<tr key={i}>
                <td>Thế hệ thứ {i + 1}</td>
                <td>{chromosome}</td>
                <td>{fitness}</td>
                <td><a onClick={() => onViewDetail(i + 1)}>Xem chi tiết</a></td>
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
