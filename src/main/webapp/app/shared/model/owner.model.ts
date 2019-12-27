import { ILedger } from 'app/shared/model/ledger.model';

export interface IOwner {
  id?: number;
  name?: string;
  ledgers?: ILedger[];
}

export class Owner implements IOwner {
  constructor(public id?: number, public name?: string, public ledgers?: ILedger[]) {}
}
