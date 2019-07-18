import * as statusService from './generated/StatusService';

export type SetStatusCallback = (status: string) => void;

export class StatusController {
  constructor(
    private setStatusCallback: SetStatusCallback
  ) {}

  async updateAction(newStatus: string): Promise<void> {
    if (!newStatus) {
      this.setStatusCallback('Enter a new status first!');
    } else {
      const status = (await statusService.update(newStatus)) || '';
      this.setStatusCallback(status);
    }
  }
}
