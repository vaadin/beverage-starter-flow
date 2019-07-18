import * as connectServices from './generated/ConnectServices';

export type SetStatusCallback = (status: string) => void;

export class StatusController {
  constructor(
    private setStatusCallback: SetStatusCallback
  ) {}

  async updateAction(newStatus: string): Promise<void> {
    if (!newStatus) {
      this.setStatusCallback('Enter a new status first!');
    } else {
      const status = (await connectServices.update(newStatus)) || '';
      this.setStatusCallback(status);
    }
  }
}
