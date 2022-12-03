import moment from "moment";
import 'moment/locale/vi';
moment.locale('vi')

export const displayDatetime = (datetime) => moment(datetime).format('HH:mm DD/MM/YYYY');

export const displayDate = (datetime) => moment(datetime).format('DD/MM/YYYY');

export const displayTime = (datetime) => moment(datetime).format('HH:mm');
