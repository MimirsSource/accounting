import { Moment } from 'moment';
import { IOwner } from 'app/shared/model/owner.model';

export interface ILedger {
  id?: number;
  name?: string;
  description?: string;
  creationDate?: Moment;
  owner?: IOwner;
}

export class Ledger implements ILedger {
  constructor(public id?: number, public name?: string, public description?: string, public creationDate?: Moment, public owner?: IOwner) {}
}
