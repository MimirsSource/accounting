import { Moment } from 'moment';
import { ILedger } from 'app/shared/model/ledger.model';

export interface IBalance {
  id?: number;
  date?: Moment;
  amount?: number;
  ledger?: ILedger;
}

export class Balance implements IBalance {
  constructor(public id?: number, public date?: Moment, public amount?: number, public ledger?: ILedger) {}
}
