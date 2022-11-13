const fetchData = async (url, method, data) => {
  return await fetch(url,
    {
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      method: method,
      body: data ? JSON.stringify(data) : null
    })
    .then((response) => response.json())
    .then((json) => json)
    .catch(error => {
      console.error('There was an error!', error);
    });
}

export const getData = (url) => fetchData(url, "GET", null);

export const postData = (url, data) => fetchData(url, "POST", data);

export const putData = (url, data) => fetchData(url, "PUT", data);

export const patchData = (url, data) => fetchData(url, "PATCH", data);

export const deleteData = (url) => fetchData(url, "DELETE", null);
