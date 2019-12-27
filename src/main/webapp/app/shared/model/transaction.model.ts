import { Moment } from 'moment';
import { ILedger } from 'app/shared/model/ledger.model';
import { ICategory } from 'app/shared/model/category.model';

export interface ITransaction {
  id?: number;
  name?: string;
  amount?: number;
  date?: Moment;
  initial?: boolean;
  fromLedger?: ILedger;
  toLedger?: ILedger;
  category?: ICategory;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public name?: string,
    public amount?: number,
    public date?: Moment,
    public initial?: boolean,
    public fromLedger?: ILedger,
    public toLedger?: ILedger,
    public category?: ICategory
  ) {
    this.initial = this.initial || false;
  }
}
