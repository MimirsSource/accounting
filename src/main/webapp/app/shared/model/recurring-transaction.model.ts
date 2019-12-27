import { ILedger } from 'app/shared/model/ledger.model';

export interface IRecurringTransaction {
  id?: number;
  name?: string;
  amount?: number;
  dateOfMonth?: number;
  fromLedger?: ILedger;
  toLedger?: ILedger;
}

export class RecurringTransaction implements IRecurringTransaction {
  constructor(
    public id?: number,
    public name?: string,
    public amount?: number,
    public dateOfMonth?: number,
    public fromLedger?: ILedger,
    public toLedger?: ILedger
  ) {}
}
