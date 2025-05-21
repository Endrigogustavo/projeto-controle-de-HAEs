import { repository } from "../../../database/repository";

const emailRepository = repository.emailVerification;

export const save = async (
  email: string,
  password: string,
  name: string,
  code: string,
  expiresAt: Date
) => {
  return await emailRepository.upsert({
    where: { email },
    update: { code, password, name, expiresAt },
    create: { email, code, password, name, expiresAt },
  });
};

export const findByEmail = async (email: string, code: string) => {
  const record = await emailRepository.findFirst({
    where: {
      email: email,
      code: code,
      expiresAt: { gte: new Date() },
    },
  });

  return record;
};

export const deleteByEmail = async (email: string) => {
  await emailRepository.deleteMany({ where: { email } });
};


