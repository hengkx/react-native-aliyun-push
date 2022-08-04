export interface PushMessage {
  /**
   * "notification":通知 或者 "message":消息
   */
  type: 'notification' | 'message';
  /**
   * 推送通知/消息标题
   */
  title: string;
  /**
   * 推送通知/消息具体内容
   */
  body: string;
  /**
   * "opened":用户点击了通知, "removed"用户删除了通知, 其他非空值:用户点击了自定义action（仅限ios）
   */
  actionIdentifier: string;
  /**
   * 用户附加的{key:value}的对象
   */
  extras: Record<string, any>;
}
/**
 * 目标类型
 */
export enum TargetType {
  /**
   * 本设备
   */
  CurrentDevice = 1,
  /**
   * 本设备绑定的账号
   */
  CurrentAccount = 2,
  /**
   * 别名
   */
  Alias = 3,
}

type Callback = (message: PushMessage) => void;

export default class AliyunPuth {
  static addListener(callback: Callback): void;
  static removeListener(callback: Callback): void;

  /**
   * 同步角标数到阿里云服务端 (仅ios支持)
   */
  static syncBadgeNum(num: number): void;

  /**
   * 获取初始消息
   */
  static getInitialMessage(): Promise<PushMessage>;

  /**
   * 获取设备Id
   */
  static getDeviceId(): Promise<string>;

  /**
   * 绑定账号
   * @param account 账号
   */
  static bindAccount(account: string): Promise<string>;

  /**
   * 解绑账号
   */
  static unbindAccount(): Promise<string>;

  /**
   * 绑定TAG
   * @param target 目标类型
   * @param tags 标签（数组输入）
   * @param alias 别名，仅当target=3时生效
   */
  static bindTag(target: TargetType, tags: string[], alias?: string): Promise<string>;

  /**
   * 解绑TAG
   * @param target 目标类型
   * @param tags 标签（数组输入）
   * @param alias 别名，仅当target=3时生效
   */
  static unbindTag(target: TargetType, tags: string[], alias?: string): Promise<string>;

  /**
   * 查询目标绑定的标签，当前仅支持查询设备标签。
   * @param target 目标类型 1：本设备
   */
  static listTag(target: TargetType): Promise<string[]>;

  /**
   * 添加别名
   * 单个设备最多添加128个别名，同一个别名最多可被添加到128个设备。
   * 别名支持的最大长度为128字节。
   */
  static addAlias(alias: string): Promise<string>;

  /**
   * 删除别名
   * @param alias alias = null or alias.length = 0 时，删除设备全部别名
   */
  static removeAlias(alias?: string): Promise<string>;

  /**
   * 查询别名
   */
  static listAliases(): Promise<string[]>;

  /**
   * 设置桌面图标角标数字 (ios支持，android支持绝大部分手机)
   */
  static setApplicationIconBadgeNumber(num: number): void;
}
