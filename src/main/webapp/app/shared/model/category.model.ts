import { ITransaction } from 'app/shared/model/transaction.model';

export interface ICategory {
  id?: number;
  name?: string;
  transactions?: ITransaction[];
}

export class Category implements ICategory {
  constructor(public id?: number, public name?: string, public transactions?: ITransaction[]) {}
}
